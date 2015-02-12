(ns seattle-service.routes
  (:require [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [expand-routes]]
            [hypercrud-service.pedestal-util :as pedestal-util]
            [hypercrud-service.hypercrud :as hypercrud]
            [hypercrud-service.datomic-util :refer [latest-tx]]
            [hypercrud-service.collection-json :as cj]
            [hypercrud-service.api :as api]
            [seattle-service.page :as page]
            [seattle-service.endpoints]))


(defn api-get [{{:keys [tx]} :query-params}]
  (let [tx (if tx (Long/parseLong tx) (latest-tx))]
    (-> (api/index seattle-service.endpoints/endpoints tx)
        (#(cj/->ReadCollectionResponse :seattle-service.routes/api-get nil % [] nil tx)))))


(defn mk-coll-get [coll-route item-route query typeinfo]
  (fn [{{:keys [tx]} :query-params}]
    (-> (api/collection-read coll-route
                             item-route
                             query
                             typeinfo
                             (Long/parseLong tx))
        (#(cj/->ReadCollectionResponse coll-route item-route [] % typeinfo tx)))))


(defn mk-coll-post [typeinfo]
  (fn [{:keys [:seattle-service.pedestal-util/body-params]}]
    ;; Should we also check tx (to detect concurrent modifications of collection)?
    ;; I don't think that makes sense on item creation
    (-> (api/collection-create body-params typeinfo)
        (cj/->CreateItemResponse))))


(defn mk-item-get [item-route typeinfo]
  (fn [{{:keys [tx]} :query-params {:keys [id]} :path-params}]
    (-> (api/entity-read (Long/parseLong id) (Long/parseLong tx))
        (#(cj/->ReadItemResponse item-route [] % typeinfo tx)))))


(defn mk-item-put [typeinfo]
  (fn [{{:keys [tx]} :query-params
        {:keys [id]} :path-params
        :keys [:seattle-service.pedestal-util/body-params]}]
    (-> (api/entity-update (Long/parseLong id)
                           (Long/parseLong tx) ;; to detect concurrent modifications
                           body-params
                           typeinfo)
        (cj/->UpdateItemResponse))))


(defn route [root query typeinfo]
  "see: https://groups.google.com/forum/#!topic/pedestal-users/UXezozhUFM4
   Note that query needs to be specially quoted, wtf?"
  (let [coll-route (keyword root)
        item-route (keyword (str root "-item"))]
    `[~(str "/" root) {:get [~coll-route (mk-coll-get ~coll-route ~item-route '~query ~typeinfo)]
                       :post [~(keyword root "post") (mk-coll-post ~typeinfo)]}
      ["/:id" {:get [~item-route (mk-item-get ~item-route ~typeinfo)]
               :put [~(keyword (str root "-item") "put") (mk-item-put ~typeinfo)]}]]))


(def routes
  (expand-routes
   `[[["/echo" {:any page/echo}]
      ["/api" {:get api-get}
       ^:interceptors [(body-params/body-params)
                       pedestal-util/auto-content-type
                       pedestal-util/combine-body-params
                       pedestal-util/save-body-val
                       pedestal-util/wrap-ring-response
                       hypercrud/wrap-hypercrud
                       ] ;; are these out of order??

       ~@(map (fn [[k [query typeinfo]]]
                (route k query typeinfo))
              seattle-service.endpoints/endpoints)
       ]]]))
