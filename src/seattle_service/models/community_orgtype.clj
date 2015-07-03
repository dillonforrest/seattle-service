(ns seattle-service.models.community-orgtype
  (:require [hypercrud-service.collection-json :as cj]))


(defmethod cj/route-for-entity :CommunityOrgType [typetag] :enums/community.orgtype-item)

(defmethod cj/typeinfo :CommunityOrgType [typetag tx & [record]]
  [{:name :db/ident :prompt "Ident" :datatype :keyword :set false}])
