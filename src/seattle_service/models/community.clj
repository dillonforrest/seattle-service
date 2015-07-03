(ns seattle-service.models.community
  (:require [hypercrud-service.collection-json :as cj]))


(defmethod cj/route-for-entity :Community [typetag] :communities-item)

(defmethod cj/typeinfo :Community [typetag tx & [record]]
  [{:name :community/name :prompt "Name" :datatype :string :set false}
   {:name :community/url :prompt "Url" :datatype :string :set false}
   {:name :community/neighborhood :prompt "Neighborhood" :datatype :ref :set false
    :options {:label-prop :neighborhood/name :typetag :Neighborhood}}
   {:name :community/category :prompt "Category" :datatype :string :set true}
   {:name :community/orgtype :prompt "Org type" :datatype :ref :set false
    :options {:label-prop :db/ident :typetag :CommunityOrgType}}
   {:name :community/type :prompt "Type" :datatype :ref :set true
    :options {:label-prop :db/ident :typetag :CommunityType}}
   ])
