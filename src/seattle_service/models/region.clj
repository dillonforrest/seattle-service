(ns seattle-service.models.region
  (:require [hypercrud-service.collection-json :as cj]))


(defmethod cj/route-for-entity :Region [typetag] :enums/region-item)

(defmethod cj/typeinfo :Region [typetag tx & [record]]
  [{:name :db/ident :prompt "Ident" :datatype :keyword :set false}])
