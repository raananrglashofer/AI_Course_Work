require "byebug"
user_name = ARGV[0] || "???"
byebug
puts "Hello #{user_name}"