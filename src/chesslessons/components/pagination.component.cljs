(ns chesslessons.components.pagination.components
	(:require
; 		Models
		[chesslessons.pagination-model :refer [pagination set_current_pagination_page]]
		[chesslessons.visitors-model :refer [visitors deleted_visitors]]
; 		Components
		[chesslessons.components.admin.components :refer [tab]]
		))

(def log (.-log js/console))


(defn visitors_size []
	(count (case @tab
			   :visitors @visitors
			   :deleted_visitors @deleted_visitors))
	)


(defn render_pagination_item [index current]
	[:li.page-item {:class (if (= index current) "active") :key (str current " " index)}
	 [:a.page-link {:on-click #(set_current_pagination_page index @tab)} (str (+ index 1))]])


(defn render_pagination_items []
	[:ul.pagination
	 (let [display (:display @pagination) current (@tab (:current @pagination))]
		 (for [index (range (visitors_size))]
		 ^{:key index} (render_pagination_item index current)
		 ))
	 ]
	)


(defn render []
	(if (> (visitors_size) 0)
			 [:nav
				[render_pagination_items]
		 ])
	)
