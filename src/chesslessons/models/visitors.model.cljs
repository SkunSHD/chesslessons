(ns chesslessons.visitors-model
	(:require
		[chesslessons.firebase.db :as db]
		;		Utils
		[chesslessons.atom.utils :refer [atom! action!]]
		[chesslessons.normalize-visitor.utils :refer [normalize_visitor format_visitors]]))


(def log (.-log js/console))


; ==================
; Atoms
(defonce visitors (atom! "[visitors.model/visitors]" {}))
(defonce visitors_error_msg (atom! "[visitors.model/visitors_error_msg]" ""))

(defonce deleted_visitors (atom! "[visitors.model/deleted_visitors]" {}))
(defonce deleted_visitors_error_msg (atom! "[visitors.model/deleted_visitors_error_msg]" ""))


; ==================
; Private
(defn- -format_visitors [visitors]
	(map (fn [visitor] (js->clj (.data visitor) :keywordize-keys true))
	     (aget visitors "docs")))


; ==================
; Actions
(defn set_visitors [new_visitors]
	(action! "[visitors.model/set_visitors]" new_visitors)
	(reset! visitors new_visitors))


(defn set_visitors_error_msg [errors]
	(action! "[visitors.model/set_visitors_error_msg]" errors)
	(reset! visitors_error_msg errors))


(defn set_deleted_visitors [new_deleted_visitors]
	(action! "[visitors.model/set_deleted_visitors]" new_deleted_visitors)
	(reset! deleted_visitors new_deleted_visitors))


(defn set_deleted_visitors_error_msg [errors]
	(action! "[visitors.model/set_deleted_visitors_error_msg]" errors)
	(reset! visitors_error_msg errors))
