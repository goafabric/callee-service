curl -w "Response time: %{time_total} seconds\n" -H "Authorization: BASIC YWRtaW46YWRtaW4=" "http://localhost:50900/callees/sayMyName?name=Heisenberg"
