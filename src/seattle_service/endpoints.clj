(ns seattle-service.endpoints
  (:require [seattle-service.models.community]
            [seattle-service.models.neighborhood]
            [seattle-service.models.district]
            [seattle-service.models.community-type]
            [seattle-service.models.community-orgtype]
            [seattle-service.models.region]
            [datomic.api :as d]
            [hypercrud-service.datomic-util :as datomic-util]))


(def endpoints
  ;; Should also store :rel and :prompt here.
  ;; For now, rel must line up by hand with the key here
  {"communities"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q '[:find ?e :where [?e :community/name]] tx))
    :typeinfo-fn (fn [_] seattle-service.models.community/typeinfo)}

   "neighborhoods"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q '[:find ?e :where [?e :neighborhood/name]] tx))
    :typeinfo-fn (fn [_] seattle-service.models.neighborhood/typeinfo)}

   "districts"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q '[:find ?e :where [?e :district/name]] tx))
    :typeinfo-fn (fn [_] seattle-service.models.district/typeinfo)}

   "enums/community.orgtype"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q
                        '[:find ?id
                          :where [?id :db/ident ?ident]
                          [(namespace ?ident) ?ns]
                          [(= ?ns "community.orgtype")]]
                        tx))
    :typeinfo-fn (fn [_] seattle-service.models.community-orgtype/typeinfo)}

   "enums/community.type"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q
                        '[:find ?id
                          :where [?id :db/ident ?ident]
                          [(namespace ?ident) ?ns]
                          [(= ?ns "community.type")]]
                        tx))
    :typeinfo-fn (fn [_] seattle-service.models.community-type/typeinfo)}

   "enums/region"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q
                        '[:find ?id
                          :where [?id :db/ident ?ident]
                          [(namespace ?ident) ?ns]
                          [(= ?ns "region")]]
                        tx))
    :typeinfo-fn (fn [_] seattle-service.models.region/typeinfo)}

   })


(def preds
  {:communities-item #(contains? % :community/name)
   :neighborhoods-item #(contains? % :neighborhood/name)
   :districts-item #(contains? % :district/name)
   :enums/community.type-item #(= "community.type" (-> % :db/ident namespace))
   :enums/community.orgtype-item #(= "community.orgtype" (-> % :db/ident namespace))
   :enums/region-item #(= "region" (-> % :db/ident namespace))
   })


(defn deduce-route-name [ent]
  {:post [(not (nil? %))]}
  (let [route-name (->> (filter (fn [[route pred]]
                                  (pred ent))
                                preds)
                        first
                        first)]
    (assert route-name (format "No route for (%s)" (d/touch ent)))
    route-name))
