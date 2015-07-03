(ns seattle-service.models.community-type
  (:require [hypercrud-service.collection-json :as cj]))


(defrecord CommunityType [ent])

(defmethod cj/route-for-entity CommunityType [record] :enums/community.type-item)

(defmethod cj/typeinfo CommunityType [record tx]
  [{:name :db/ident :prompt "Ident" :datatype :keyword :set false}
   ])
