(ns seattle-service.endpoints
  (:require [seattle-service.models.community]
            [seattle-service.models.neighborhood]
            [seattle-service.models.district]
            [seattle-service.models.community-type]
            [seattle-service.models.community-orgtype]
            [seattle-service.models.region]
            [datomic.api :as d]))


(def endpoints
  ;; Should also store :rel and :prompt here.
  ;; For now, rel must line up by hand with the key here
  {"communities" ['[:find ?e :where [?e :community/name]]
                  seattle-service.models.community/typeinfo]

   "neighborhoods" ['[:find ?e :where [?e :neighborhood/name]]
                    seattle-service.models.neighborhood/typeinfo]

   "districts" ['[:find ?e :where [?e :district/name]]
                seattle-service.models.district/typeinfo]

   "enums/community.orgtype" ['[:find ?id
                                :where [?id :db/ident ?ident]
                                [(namespace ?ident) ?ns]
                                [(= ?ns "community.orgtype")]]
                              seattle-service.models.community-orgtype/typeinfo]

   "enums/community.type" ['[:find ?id
                             :where [?id :db/ident ?ident]
                             [(namespace ?ident) ?ns]
                             [(= ?ns "community.type")]]
                           seattle-service.models.community-type/typeinfo]

   "enums/region" ['[:find ?id
                     :where [?id :db/ident ?ident]
                     [(namespace ?ident) ?ns]
                     [(= ?ns "region")]]
                   seattle-service.models.region/typeinfo]

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
