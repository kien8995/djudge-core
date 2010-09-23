for %%i in (src/*.cpp) do (
	cp src/%%i %%i
	call cl.exe %%i
)
