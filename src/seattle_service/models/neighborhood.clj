(ns seattle-service.models.neighborhood
  (:require [hypercrud-service.collection-json :as cj]))


(defrecord Neighborhood [ent])

(defmethod cj/route-for-entity Neighborhood [record] :neighborhoods-item)

(defmethod cj/typeinfo Neighborhood [record tx]
  [{:name :neighborhood/name :prompt "Name" :datatype :string :set false}
   {:name :neighborhood/district :prompt "District" :datatype :ref :set false
    :options {:label-prop :district/name :route :districts}}
   ])
