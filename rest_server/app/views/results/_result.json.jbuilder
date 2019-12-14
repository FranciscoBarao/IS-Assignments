json.set! :name, Information.find(result.information_id).name if result.information_id.positive?
json.extract! result, :data_type, :value
