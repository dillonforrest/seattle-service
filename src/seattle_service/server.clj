(ns streaker-service.server
  (:gen-class) ; for -main method in uberjar
  (:require [io.pedestal.http :as bootstrap]
            [hypercrud-service.hypercrud :as hypercrud]
            [streaker-service.endpoints]
            [streaker-service.service]
            [streaker-service.service-instance]))


(extend-type Object
  hypercrud/HypercrudDependencies
  (deduce-route-name [ent]
    (streaker-service.endpoints/deduce-route-name ent))
  (service-instance [_]
    streaker-service.service-instance/service-instance))


(defn create-server [& [opts]]
  (alter-var-root #'streaker-service.service-instance/service-instance
                  (constantly
                   (bootstrap/create-server
                    (merge streaker-service.service/service opts)))))

(defn -main [& args]
  (create-server)
  (bootstrap/start streaker-service.service-instance/service-instance))
