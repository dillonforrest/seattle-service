(ns streaker-service.models.neighborhood)

(defn typeinfo [& [instance]]
  [{:name :neighborhood/name :prompt "Name" :datatype :string :set false}
   {:name :neighborhood/district :prompt "District" :datatype :ref :set false
    :options {:label-prop :district/name :route :districts}}
   ])
