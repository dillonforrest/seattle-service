(ns streaker-service.models.community)


(comment
  :datatype :ref :many :true ;; multiselect combobox (entity or dict or enum)
  :datatype :string :many :true ;; list of strings
  :datatype :ref ;; combobox (entity or dict or enum)

  ;; Then all :ref can go through lazy-cj-item, including normalizing any
  ;; datomic idents into their lazy-cj-items

  ;; Note that all :ref's have :option's

  )

(defn typeinfo [& [instance]]
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


;; Url to the options, or inline options
;; But if we're linking to a collection, we need the route only.
;; Can use the CJ standard to execute queries.

;; id on the client is always the href of the option, if i can make the
;; service understand that or parse the entid out of the request
