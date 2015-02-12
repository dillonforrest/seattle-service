(ns seattle-service.service
  (:require [seattle-service.routes]
            [io.pedestal.http :as bootstrap]))


(def service {:env :prod
              ::bootstrap/routes seattle-service.routes/routes
              ::bootstrap/resource-path "/public"
              ::bootstrap/type :jetty
              ;;::bootstrap/host "localhost"
              ::bootstrap/port 8080})
