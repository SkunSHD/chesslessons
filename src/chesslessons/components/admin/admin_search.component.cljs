(ns chesslessons.components.admin_search.component
	(:require
		[chesslessons.search-model :refer [on_search_change]]))

(def log (.-log js/console))



(defn render_admin_search []
	[:span.d-inline-block
	 {:style {:display "inline"} :tab-index "0" :data-toggle "tooltip"}
	 [:input.form-control.mr-sm-2
	  {:type        "search"
	   :placeholder "Search..."
	   :aria-label  "Search"
	   :on-change   on_search_change
	   :title       "Enter name or email"}]])
