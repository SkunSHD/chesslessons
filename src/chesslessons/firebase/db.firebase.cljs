(ns chesslessons.firebase.db
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fb]))

(def log (.-log js/console))


(def firestore (.firestore js/firebase))

(def collections {
	 :visitors (.collection firestore "visitors")
	 :deleted_visitors (.collection firestore "deleted_visitors")
})


; ==============
; Private
(defn- -visitors_exists? [visitors]
	(not (empty?(filter
	             (fn [visitor] (aget visitor "exists"))
	             (js->clj (aget visitors "docs"))))))


; ==================
; Public
(defn get_visitor_by_email [email]
	(.get (.where (:visitors collections) "email" "==" email)))

(defn get_visitor_by_uid [uid]
	(.get (.doc (:visitors collections) uid)))

(defn get_all_visitors []
	(.get (:visitors collections)))

(defn get_all_deleted_visitors []
	(.get (:deleted_visitors collections)))

(defn save_visitor [new_visitor]
	(.then (get_visitor_by_email (:email new_visitor)) (fn [visitors]
		(if (-visitors_exists? visitors)
		    (log "Visitor already exists: " (:email new_visitor))
			(.set (.doc (:visitors collections) (:uid new_visitor)) (clj->js(merge new_visitor { :timestamp (.now js/Date) }))) )
		))
	)

(defn save_deleted_visitor [visitor]
	(let [visitor_data (.data visitor)]
		(.set (.doc (:deleted_visitors collections) (aget visitor_data "uid")) visitor_data))
	)

(defn backup_visitor [uid]
	(.then (get_visitor_by_uid uid) save_deleted_visitor))

(defn delete_visitor [uid]
	(.then (backup_visitor uid) (fn []
			(.then (.delete (.doc (:visitors collections) uid))
				   #(log "delete visitor success, uid:" uid)
			   #(log "delete visitor error" %))))
	)

(defn add_listener_on_collection [collection_name handler]
	(.onSnapshot (collection_name collections) handler))
