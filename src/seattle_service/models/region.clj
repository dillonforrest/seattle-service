(ns seattle-service.models.region
  (:require [hypercrud-service.collection-json :as cj]))


(defrecord Region [ent])

(defmethod cj/route-for-entity Region [record] :enums/region-item)

(defmethod cj/typeinfo Region [record tx]
  [{:name :db/ident :prompt "Ident" :datatype :keyword :set false}])
