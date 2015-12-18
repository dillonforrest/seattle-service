(ns seattle-service.models.mvmvp
  (:require [hypercrud-service.collection-json :as cj]))

(defmethod cj/route-for-entity :Mvmvp [typetag] :mvmvps-item)

(defmethod cj/typeinfo :Mvmvp [typetag tx & [record]]
  [{:name :mvmvp/email :prompt "What is your email?"
    :datatype :string :set false}
   {:name :mvmvp/value-prop :prompt "What's your idea's value prop in 1 sentence?"
    :datatype :string :set false}
   {:name :mvmvp/name :prompt "What's your startup's name?"
    :datatype :string :set false}
   {:name :mvmvp/header :prompt "What's your header?"
    :datatype :string :set false}
   {:name :mvmvp/subheader :prompt "What's your subheader?"
    :datatype :string :set false}
   {:name :mvmvp/steps :prompt "How can we use your idea in 3 steps?"
    :datatype :string :set false}
   ])