(ns seattle-service.models.community-orgtype
  (:require [hypercrud-service.collection-json :as cj]))


(defrecord CommunityOrgType [ent])

(defmethod cj/route-for-entity CommunityOrgType [record] :enums/community.orgtype-item)

(defmethod cj/typeinfo CommunityOrgType [record tx]
  [{:name :db/ident :prompt "Ident" :datatype :keyword :set false}])
