(ns seattle-service.routes
  (:require [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [expand-routes]]
            [hypercrud-service.pedestal-util :as pedestal-util]
            [hypercrud-service.core :as hypercrud]
            [seattle-service.page :as page]
            [seattle-service.endpoints]))


(def routes
  (expand-routes
   `[[["/echo" {:any page/echo}]
      ["/api" {:get [:api-get (hypercrud/mk-api-get :api-get seattle-service.endpoints/endpoints)]}
       ^:interceptors [(body-params/body-params)
                       pedestal-util/auto-content-type
                       pedestal-util/combine-body-params
                       pedestal-util/save-body-val
                       pedestal-util/wrap-ring-response
                       hypercrud/wrap-hypercrud
                       ] ;; are these out of order??

       ~@(map (fn [[k {:keys [query-fn typetag]}]]
                (hypercrud/route k query-fn typetag))
              seattle-service.endpoints/endpoints)
       ]]]))
