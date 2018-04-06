(ns chesslessons.components.pagination.component
	(:require
; 		Models
		[chesslessons.pagination-model :refer [pagination set_current_pagination_page]]
		[chesslessons.visitors-model :refer [visitors deleted_visitors]]
		))


(def log (.-log js/console))


(defn visitors_size [tab]
	(count (case @tab
			   :all_visitors (get_all_visitors)
			   :visitors @visitors
			   :deleted_visitors @deleted_visitors
			   ))
	)


(defn render_pagination_item [index current tab]
	[:li.page-item {:class (if (= index current) "active") :key (str current " " index)}
	 [:a.page-link {:on-click #(set_current_pagination_page index @tab)} (str (+ index 1))]])


(defn render_pagination_items [tab]
	(let [tab_prop tab]
		[:ul.pagination
		 (let [display (:display @pagination)
			   current (@tab_prop (:current @pagination))]
			 (for [index (range (visitors_size tab_prop))]
				 ^{:key index} (render_pagination_item index current tab_prop)))]))


(defn render [tab]
	(let [tab_prop tab]
		(if (> (visitors_size tab_prop) 1)
			[:nav
			 [render_pagination_items tab_prop]
			 ]))
	)
