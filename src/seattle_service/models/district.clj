(ns seattle-service.models.district
  (:require [hypercrud-service.collection-json :as cj]))


(defrecord District [ent])

(defmethod cj/route-for-entity District [record] :districts-item)

(defmethod cj/typeinfo District [record tx]
  [{:name :district/name :prompt "Name" :datatype :string :set false}
   {:name :district/region :prompt "Region" :datatype :ref :set false
    :options {:label-prop :db/ident :route :enums/region}}
   ])
