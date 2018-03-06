(ns chesslessons.visitors-model
	(:require
		[chesslessons.firebase.db :as db]
;		Utils
		[chesslessons.atom.utils :refer [atom! action!]]
		[chesslessons.normalize-visitor.utils :refer [normalize_visitor format_visitors]]
; 		Models
		[chesslessons.pagination-model :refer [pagination]]
		))


(def log (.-log js/console))


; ==================
; Atoms
(defonce visitors (atom! "[visitors.model/visitors]" '()))
(defonce visitors_error_msg (atom! "[visitors.model/visitors_error_msg]" ""))

(defonce deleted_visitors (atom! "[visitors.model/deleted_visitors]" '()))
(defonce deleted_visitors_error_msg (atom! "[visitors.model/deleted_visitors_error_msg]" ""))


; ==================
; Private
(defn- -format_visitors [visitors]
	(map (fn [visitor] (js->clj (.data visitor) :keywordize-keys true))
	     (aget visitors "docs")))


(defn- -chunck_visitors_for_pagination [visitors]
	(partition (:display @pagination) nil nil visitors))


; ==================
; Actions
(defn set_visitors [new_visitors]
	(let [chunked_visitors_list (-chunck_visitors_for_pagination new_visitors)]
		(action! "[visitors.model/set_visitors]" chunked_visitors_list)
		(reset! visitors chunked_visitors_list))
	)


(defn set_visitors_error_msg [errors]
	(action! "[visitors.model/set_visitors_error_msg]" errors)
	(reset! visitors_error_msg errors))


(defn set_deleted_visitors [new_deleted_visitors]
	(let [chunked_deleted_visitors_list (-chunck_visitors_for_pagination new_deleted_visitors)]
		(action! "[visitors.model/set_deleted_visitors]" chunked_deleted_visitors_list)
		(reset! deleted_visitors chunked_deleted_visitors_list))
	)


(defn set_deleted_visitors_error_msg [errors]
	(action! "[visitors.model/set_deleted_visitors_error_msg]" errors)
	(reset! visitors_error_msg errors))


(defn get_current_page_visitors [collection_name]
	(let [pagination_current_page (collection_name (:current @pagination))
		  collection_current (case collection_name
								 :visitors @visitors
								 :deleted_visitors @deleted_visitors)
		  ]
		(if (> (count collection_current) 0)
					(nth collection_current pagination_current_page))))
