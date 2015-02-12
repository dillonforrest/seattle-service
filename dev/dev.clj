(ns dev
  (:require [datomic.api :as d]
            [datomico.core :as dc]
            [datomico.db :as db]
            [io.pedestal.http :as bootstrap]
            [hypercrud-service.datomic-util :refer [with-db-as-of latest-tx]]
            [streaker-service.service]
            [streaker-service.routes]
            [streaker-service.server]
            [streaker-service.service-instance]
            [ns-tracker.core :as tracker]))

(def service (-> streaker-service.service/service ;; start with production configuration
                 (merge  {:env :dev
                          ::bootstrap/join? false
                          ::bootstrap/routes #(deref #'streaker-service.routes/routes)
                          ::bootstrap/allowed-origins {:creds true
                                                       :allowed-origins (constantly true)}})
                 (bootstrap/default-interceptors)
                 (bootstrap/dev-interceptors)))

(defn start
  [& [opts]]
  (streaker-service.server/create-server (merge service opts))
  (bootstrap/start streaker-service.service-instance/service-instance))

(defn stop []
  (bootstrap/stop streaker-service.service-instance/service-instance))

(defn restart []
  (stop)
  (start))

(defn- ns-reload [track]
  (try
    (doseq [ns-sym (track)]
      (require ns-sym :reload))
    (catch Throwable e (.printStackTrace e))))

(defn watch
  ([] (watch ["src"]))
  ([src-paths]
     (let [track (tracker/ns-tracker src-paths)
           done (atom false)]
       (doto
           (Thread. (fn []
                      (while (not @done)
                        (ns-reload track)
                        (Thread/sleep 500))))
         (.setDaemon true)
         (.start))
       (fn [] (swap! done not)))))


(defn start-dev-db []
  (dc/start
   {:schemas []
    :seed-data []
    :dynamic-vars true})

  (with-db-as-of (latest-tx)
    @(d/transact db/*connection* (read-string (slurp "resources/seattle-schema.edn")))
    @(d/transact db/*connection* (read-string (slurp "resources/seattle-data0.edn")))))


(defn -main [& args]
  (start-dev-db)
  (start)
  (watch))
