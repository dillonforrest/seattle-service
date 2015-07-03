(ns seattle-service.models.community
  (:require [hypercrud-service.collection-json :as cj]))


(defrecord Community [ent])

(defmethod cj/route-for-entity Community [record] :communities-item)

(defmethod cj/typeinfo Community [record tx]
  [{:name :community/name :prompt "Name" :datatype :string :set false}
   {:name :community/url :prompt "Url" :datatype :string :set false}
   {:name :community/neighborhood :prompt "Neighborhood" :datatype :ref :set false
    :options {:label-prop :neighborhood/name :route :neighborhoods}}
   {:name :community/category :prompt "Category" :datatype :string :set true}
   {:name :community/orgtype :prompt "Org type" :datatype :ref :set false
    :options {:label-prop :db/ident :route :enums/community.orgtype}}
   {:name :community/type :prompt "Type" :datatype :ref :set true
    :options {:label-prop :db/ident :route :enums/community.type}}
   ])
