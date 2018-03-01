(ns chesslessons.admin-model
	(:require
		[chesslessons.firebase :as fbs]
		[chesslessons.firebase.db :as db]
;		Utils
		[chesslessons.atom.utils  :refer [atom! action!]]
		[chesslessons.normalize-visitor.utils :refer [normalize_visitor format_visitors]]
; 		Models
		[chesslessons.visitor-model :refer [set_visitor]]
		[chesslessons.visitors-model :as visitors_model]
		))


(def log (.-log js/console))


; ==================
; Atoms
(defonce admin (atom! "[admin.model/admin]" nil))
(defonce sign_in_error_msg (atom! "[admin.model/sign_in_error_msg]" ""))


; ==================
; Actions
(defn set_sign_in_error_msg [new_msg]
	(action! "[admin.model/set_sign_in_error_msg]" new_msg)
	(reset! sign_in_error_msg new_msg))


(defn set_admin [new_admin]
	(action! "[admin.model/set_admin]" new_admin)
	(if (nil? new_admin)
		(reset! admin new_admin)
		(reset! admin (normalize_visitor new_admin))
		))


(defn sign_in_admin [email password]
	(action! "[admin.model/sign_in_admin]" email password)
	(.catch (.then
	(fbs/sign_in_with_email_and_password email password)
			(fn [new_admin] (set_admin new_admin)))
	        (fn [error] (set_sign_in_error_msg (.-message error)))
	        ))


(defn log_out_admin []
	(fbs/unsubscribe_collections)
	(.catch
	(.then (fbs/sign_out)
       (fn []
			(action! "[admin.model/log_out_admin]")))
	   (fn [error]
			(action! "[admin.model/log_out_admin]" error)
			(log error "sss"))))


; ==================
; Privat
(defn- -?admin [admin]
	(= (aget admin "email") "admin@i.ua"))


(defn- -on_visitors_collection_change [collection_name]
	(fn [visitors]
		(let [visitors_formatted (if (aget visitors "empty") {} (format_visitors visitors))]
			(action! (str "[visitors.model/on_collection_change] " collection_name)  visitors_formatted)
			(case collection_name
				:visitors (visitors_model/set_visitors  visitors_formatted)
				:deleted_visitors (visitors_model/set_deleted_visitors  visitors_formatted))
			)
	)
)


(defn- -add_collection_change_listener [collection_name]
	(action! (str "[admin.model/add_collection_change_listener] " collection_name))
	(if (nil? (collection_name @fbs/unsubscribe_collection_functions))
		(let [unsubscribe_collection_func (db/add_listener_on_collection collection_name (-on_visitors_collection_change collection_name))]
			(fbs/set_unsubscribe_collection_func collection_name unsubscribe_collection_func)))
	)


(defn -add_db_change_listeners []
	(-add_collection_change_listener :visitors)
	(-add_collection_change_listener :deleted_visitors)
	)

; ==================
; Watchers
(defn- -on_change_admin [key atom old new]
	(if (and (nil? old) (not= old new))
		(-add_db_change_listeners)))

(add-watch admin "[admin.model] ADMIN-MODEL-CHANGE-ADMIN" -on_change_admin)


; ==================
; Auth
(defn auth_state_change_handler [admin_or_visitor]
	(if (or (nil? admin_or_visitor)
			(and admin_or_visitor (-?admin admin_or_visitor)))
		(set_admin admin_or_visitor)))

(defn auth_init []
	(.onAuthStateChanged (fbs/auth) auth_state_change_handler))


