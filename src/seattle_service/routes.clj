(ns seattle-service.routes
  (:require [io.pedestal.http.route.definition :refer [expand-routes]]
            [hypercrud-service.core :as hypercrud]
            [seattle-service.page :as page]
            [seattle-service.endpoints]))


(def routes
  (expand-routes
   `[[["/echo" {:any page/echo}]
      ~(hypercrud/route "/api" seattle-service.endpoints/endpoints)
      ]]))
