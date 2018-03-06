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


(defn delete_visitor_complitly [uid]
	(.then (.delete (.doc (:deleted_visitors collections) uid))
		   #(log "delete visitor success, uid:" uid)
		   #(log "delete visitor error" %))
	)


(defn restore_deleted_visitor [uid]
	; 1 read visitor from deleted collection
	; 2 write visitor in normal collection
	; 3 delete visitor from deleted collection
	(.then (.get (.doc (:deleted_visitors collections) uid))
		   (fn [deleted_visitor]

			   (let [deleted_visitor_data  (.data deleted_visitor)]
				   (.then (.set (.doc (:visitors collections) (aget deleted_visitor_data "uid")) deleted_visitor_data)
					   #(delete_visitor_complitly uid)))))
	)

(defn add_listener_on_collection [collection_name handler]
	(.onSnapshot (collection_name collections) handler))
