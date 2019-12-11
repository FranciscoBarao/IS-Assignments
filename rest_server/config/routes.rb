Rails.application.routes.draw do
  get 'list/:type', to: 'information#index'
  resources :information, except: :index
  resources :results
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
