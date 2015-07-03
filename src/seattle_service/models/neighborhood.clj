(ns seattle-service.models.neighborhood
  (:require [hypercrud-service.collection-json :as cj]))


(defmethod cj/route-for-entity :Neighborhood [typetag] :neighborhoods-item)

(defmethod cj/typeinfo :Neighborhood [typetag tx & [record]]
  [{:name :neighborhood/name :prompt "Name" :datatype :string :set false}
   {:name :neighborhood/district :prompt "District" :datatype :ref :set false
    :options {:label-prop :district/name :typetag :District}}
   ])
