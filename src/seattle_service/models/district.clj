(ns streaker-service.models.district)

(defn typeinfo [& [instance]]
  [{:name :district/name :prompt "Name" :datatype :string :set false}
   {:name :district/region :prompt "Region" :datatype :ref :set false
    :options {:label-prop :db/ident :route :enums/region}}
   ])
