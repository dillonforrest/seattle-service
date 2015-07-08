(ns seattle-service.models.district
  (:require [hypercrud-service.collection-json :as cj]))


(defmethod cj/route-for-entity :District [typetag] :districts-item)

(defmethod cj/typeinfo :District [typetag tx & [record]]
  [{:name :district/name :prompt "Name" :datatype :string :set false}
   {:name :district/region :prompt "Region" :datatype :ref :set false
    :options {:label-prop :db/ident :typetag :Region :route :enums/region}}
   ])
