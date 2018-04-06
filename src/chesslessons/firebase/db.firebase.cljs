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

(defn- -save_deliting_visitor [visitor]
	(let [visitor_data (.data visitor)]
		(.set (.doc (:deleted_visitors collections) (aget visitor_data "uid")) visitor_data))
	)

; ==================
; Public
(defn get_visitor_by_email [email]
	(.get (.where (:visitors collections) "email" "==" email)))


(defn get_visitor_by_uid [uid collection_name]
	(.get (.doc (collection_name collections) uid)))


(defn save_visitor [new_visitor visitor_message]
	(log 1 "save_visitor")
	(.then (get_visitor_by_email (:email new_visitor)) (fn [visitors]
		(if (-visitors_exists? visitors)
		    (log "Visitor already exists: " (:email new_visitor))
			(.set (.doc (:visitors collections) (:uid new_visitor)) (clj->js (merge new_visitor { :timestamp (.now js/Date) :message visitor_message}))) )
		))
	)


(defn save_anonymous_message [phone message]
	(let [timestamp (.now js/Date) uid (str phone timestamp) new_anonymous_entry (clj->js
																					 {:uid    uid
																					  :is_anonymous true
																					  :phone     phone
																					  :message   message
																					  :timestamp timestamp
																					  :photo "https://anac.mx/wp-content/uploads/2016/08/no_image_user.png"})]
		(.catch
			(.then (.set (.doc (:anonymous_visitors collections) uid) new_anonymous_entry) #(log "save_anonymous_message success"))
			#(log "save_anonymous_message error" %))
		)
	)


(defn backup_visitor [uid collection_name]
	(.then (get_visitor_by_uid uid collection_name) -save_deliting_visitor))


(defn delete_visitor [uid collection_name]
	(.then (backup_visitor uid collection_name) (fn []
			(.then (.delete (.doc (collection_name collections) uid))
				   #(log "delete visitor success, uid:" uid)
			   #(log "delete visitor error" %))))
	)


(defn delete_visitor_complitly [uid]
	(.then (.delete (.doc (:deleted_visitors collections) uid))
		   #(log "delete visitor success, uid:" uid)
		   #(log "delete visitor error" %))
	)


(defn restore_deleted_visitor [uid restore_in_collection_name]
	; 1 read visitor from deleted collection
	; 2 write visitor in normal collection
	; 3 delete visitor from deleted collection
	(.then (.get (.doc (:deleted_visitors collections) uid))
		   (fn [deleted_visitor]

			   (let [deleted_visitor_data  (.data deleted_visitor)]
				   (.then (.set (.doc (restore_in_collection_name collections) (aget deleted_visitor_data "uid")) deleted_visitor_data)
					   #(delete_visitor_complitly uid)))))
	)

(defn add_listener_on_collection [collection_name handler]
	(.onSnapshot (collection_name collections) handler))
