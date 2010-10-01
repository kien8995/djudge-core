{- Author: Robin Nittka -}
{-# OPTIONS_GHC -O2 #-}

module Main
	where

import System.IO
import Data.Array
import Data.List
import Data.Maybe

{-
check :: [Int] -> String
check list = case (any ordered tris, rec (array ! 1) == len) of
		(False,True) -> "OK"
		(True,True) -> "ordered " ++ (show $ fromJust $ find ordered tris)
		(False,False) -> "not permutation " ++ (show $ rec (array ! 1)) ++ " " ++ show len
		(True,False) -> "totally wrong"
	where
		tris = zip3 list (drop 1 list) (drop 2 list)
		ordered (a,b,c) = (a < b && b < c) || (a > b && b > c)
		len = length list
		array = listArray (1,len) list
		rec 1 = 1
		rec pos = 1 + rec (array ! pos)
-}


build :: Int -> [Bool] -> ([Int], [Int])
build n hops = build_ n [2..] [n,n-1..] hops True
	where
		build_ 1 _ _ _ _ = ([],[])
		build_ n small (large:larges) (True:hops) True = (large:a,b)
			where (a,b) = build_ (n-1) small larges hops False
		build_ n (small:smalls) large (False:hops) True = (small:a,b)
			where (a,b) = build_ (n-1) smalls large hops True
		build_ n small (large:larges) (False:hops) False = (a,large:b)
			where (a,b) = build_ (n-1) small larges hops False
		build_ n (small:smalls) large (True:hops) False = (a,small:b)
			where (a,b) = build_ (n-1) smalls large hops True

group2 :: [Int] -> [[Int]]
group2 [] = []
group2 (a:b:l) = [a,b]:group2 l

construct :: Int -> [Int]
construct n = a ++ [mid] ++ reverse b
	where (mid,(a,b)) = case n `mod` 4 of
		0 -> (1,build n [even i | i <- [1..]])
		1 -> (n,([],reverse $ concat $ reverse $ group2 $ [1..n-1]))
		2 -> (1,build n (True:[odd i | i <- [1..]]))
		3 -> (1,build n [odd i | i <- [1..]])

main :: IO ()
main = do
--	file <- openFile "cyclic.in" ReadMode
--	hGetContents file >>= mapM_ (print . check . construct . read) . init . words
	file <- openFile "cyclic.in" ReadMode
	hGetContents file >>= mapM_ (putStrLn . unwords . map show . construct . read) . init . words

