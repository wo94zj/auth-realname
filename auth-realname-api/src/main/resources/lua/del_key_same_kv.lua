--1:true; 0:false
if redis.call("get", KEYS[1]) == ARGV[1] then
	return redis.call("del", KEYS[1]);
end

return 0;