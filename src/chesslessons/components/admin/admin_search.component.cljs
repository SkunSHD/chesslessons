(ns chesslessons.components.admin_search.component
	(:require
		[chesslessons.search-model :refer [on_search_change]]))

(def log (.-log js/console))



(defn render_admin_search []
	[:form.form-inline.my-2.my-lg-0
	 [:input.form-control.mr-sm-2 {:type "search"
								   :placeholder "search"
								   :aria-label "Search"
								   :on-change on_search_change}]
	 ])