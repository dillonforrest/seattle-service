(ns dev
  (:require [datomic.api :as d]
            [datomico.core :as dc]
            [datomico.db :as db]
            [io.pedestal.http :as bootstrap]
            [hypercrud-service.datomic-util :refer [with-db-as-of latest-tx]]
            [seattle-service.service :as service]
            [seattle-service.routes :as routes]
            [seattle-service.server :as server]
            [seattle-service.service-instance :as service-instance]
            [ns-tracker.core :as tracker]))

(def service (-> service/service ;; start with production configuration
                 (merge  {:env :dev
                          ::bootstrap/join? false
                          ::bootstrap/routes #(deref #'routes/routes)
                          ::bootstrap/allowed-origins {:creds true
                                                       :allowed-origins (constantly true)}})
                 (bootstrap/default-interceptors)
                 (bootstrap/dev-interceptors)))

(defn start
  [& [opts]]
  (server/create-server (merge service opts))
  (bootstrap/start service-instance/service-instance))

(defn stop []
  (bootstrap/stop service-instance/service-instance))

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
