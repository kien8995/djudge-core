for %%i in src/*.cpp do
	copy src/%%i %%i
	call g++ %%i -o %%i
done
