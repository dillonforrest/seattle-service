(ns seattle-service.server
  (:gen-class) ; for -main method in uberjar
  (:require [io.pedestal.http :as bootstrap]
            [hypercrud-service.dependencies :refer [HypercrudDependencies]]
            [seattle-service.endpoints]
            [seattle-service.service]
            [seattle-service.service-instance]))


(extend-type Object
  HypercrudDependencies
  (deduce-route-name [ent]
    (seattle-service.endpoints/deduce-route-name ent))
  (service-instance [_]
    seattle-service.service-instance/service-instance))


(defn create-server [& [opts]]
  (alter-var-root #'seattle-service.service-instance/service-instance
                  (constantly
                   (bootstrap/create-server
                    (merge seattle-service.service/service opts)))))

(defn -main [& args]
  (create-server)
  (bootstrap/start seattle-service.service-instance/service-instance))
