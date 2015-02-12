(ns streaker-service.endpoints
  (:require [streaker-service.models.community]
            [streaker-service.models.neighborhood]
            [streaker-service.models.district]
            [streaker-service.models.community-type]
            [streaker-service.models.community-orgtype]
            [streaker-service.models.region]
            [datomic.api :as d]))


(def endpoints
  ;; Should also store :rel and :prompt here.
  ;; For now, rel must line up by hand with the key here
  {"communities" ['[:find ?e :where [?e :community/name]]
                  streaker-service.models.community/typeinfo]

   "neighborhoods" ['[:find ?e :where [?e :neighborhood/name]]
                    streaker-service.models.neighborhood/typeinfo]

   "districts" ['[:find ?e :where [?e :district/name]]
                streaker-service.models.district/typeinfo]

   "enums/community.orgtype" ['[:find ?id
                                :where [?id :db/ident ?ident]
                                [(namespace ?ident) ?ns]
                                [(= ?ns "community.orgtype")]]
                              streaker-service.models.community-orgtype/typeinfo]

   "enums/community.type" ['[:find ?id
                             :where [?id :db/ident ?ident]
                             [(namespace ?ident) ?ns]
                             [(= ?ns "community.type")]]
                           streaker-service.models.community-type/typeinfo]

   "enums/region" ['[:find ?id
                     :where [?id :db/ident ?ident]
                     [(namespace ?ident) ?ns]
                     [(= ?ns "region")]]
                   streaker-service.models.region/typeinfo]

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
