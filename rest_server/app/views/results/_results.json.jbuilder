@results.each do |result|
  json.extract! result, :data_type, :value, :information_id
end