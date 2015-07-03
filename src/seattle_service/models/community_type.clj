(ns seattle-service.models.community-type
  (:require [hypercrud-service.collection-json :as cj]))


(defmethod cj/route-for-entity :CommunityType [typetag] :enums/community.type-item)

(defmethod cj/typeinfo :CommunityType [typetag tx & [record]]
  [{:name :db/ident :prompt "Ident" :datatype :keyword :set false}
   ])
