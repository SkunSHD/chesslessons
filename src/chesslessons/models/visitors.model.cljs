(ns chesslessons.visitors-model
    (:require
        [chesslessons.firebase.db :as db]
;		Utils
        [chesslessons.atom.utils  :refer [atom! action!]]
        [chesslessons.normalize-user.utils :refer [normalize_user]]))


(def log (.-log js/console))


; ==================
; Atoms
(defonce visitors (atom! "[visitors.model/visitors]" {}))
(defonce visitors_error_msg (atom! "[visitors.model/visitors_error_msg]" ""))



; ==================
; Private
(defn- -format_visitors [visitors]
	(map (fn [visitor] (js->clj (.data visitor) :keywordize-keys true)) (aget visitors "docs")))



; ==================
; Actions
(defn set_visitors [new_visitors]
	(action! "[visitors.model/set_visitors]" new_visitors)
    (reset! visitors new_visitors))


(defn set_visitors_error_msg [errors]
	(action! "[visitors.model/set_visitors_error_msg]" errors)
    (reset! visitors_error_msg errors))


(defn get_visitors []
	(action! "[visitors.model/get_visitors]")
    (.catch (.then (db/get_user_all)
		(fn [visitors] (set_visitors(-format_visitors visitors))))
		(fn [error] set_visitors_error_msg (.-message error))))


(defn add_listener_visitors_change []
	(action! "[visitors.model/add_visitors_change_listener]")
	(db/add_listener_on_users_collection
	  (fn [visitors]
		  (set_visitors(-format_visitors visitors))))
	)