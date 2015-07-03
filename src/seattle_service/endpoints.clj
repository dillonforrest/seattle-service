(ns seattle-service.endpoints
  (:require [seattle-service.models.community :as community]
            [seattle-service.models.neighborhood :as neighborhood]
            [seattle-service.models.district :as district]
            [seattle-service.models.community-type :as community-type]
            [seattle-service.models.community-orgtype :as community-orgtype]
            [seattle-service.models.region :as region]
            [datomic.api :as d]
            [hypercrud-service.datomic-util :as datomic-util]))


(def endpoints
  ;; Should also store :rel and :prompt here.
  ;; For now, rel must line up by hand with the key here
  {"communities"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q '[:find ?e :where [?e :community/name]] tx))
    :type-fn community/->Community}

   "neighborhoods"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q '[:find ?e :where [?e :neighborhood/name]] tx))
    :type-fn neighborhood/->Neighborhood}

   "districts"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q '[:find ?e :where [?e :district/name]] tx))
    :type-fn district/->District}

   "enums/community.orgtype"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q
                        '[:find ?id
                          :where [?id :db/ident ?ident]
                          [(namespace ?ident) ?ns]
                          [(= ?ns "community.orgtype")]]
                        tx))
    :type-fn community-orgtype/->CommunityOrgType}

   "enums/community.type"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q
                        '[:find ?id
                          :where [?id :db/ident ?ident]
                          [(namespace ?ident) ?ns]
                          [(= ?ns "community.type")]]
                        tx))
    :type-fn community-type/->CommunityType}

   "enums/region"
   {:query-fn (fn [tx] (datomic-util/datomic-simple-q
                        '[:find ?id
                          :where [?id :db/ident ?ident]
                          [(namespace ?ident) ?ns]
                          [(= ?ns "region")]]
                        tx))
    :type-fn region/->Region}

   })


;(def preds
;  {:communities-item #(contains? % :community/name)
;   :neighborhoods-item #(contains? % :neighborhood/name)
;   :districts-item #(contains? % :district/name)
;   :enums/community.type-item #(= "community.type" (-> % :db/ident namespace))
;   :enums/community.orgtype-item #(= "community.orgtype" (-> % :db/ident namespace))
;   :enums/region-item #(= "region" (-> % :db/ident namespace))
;   })
;
;
;;; This should just be a multimethod on types that wrap the entity
;(defn deduce-route-name [ent]
;  {:post [(not (nil? %))]}
;  (let [route-name (->> (filter (fn [[route pred]]
;                                  (pred ent))
;                                preds)
;                        first
;                        first)]
;    (assert route-name (format "No route for (%s)" (d/touch ent)))
;    route-name))
