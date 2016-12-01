# Build Instructions
This has been built on Java JDK 1.8.0_65 Mac OS by maven 3.3.9
 
> mvn clean package
 
# Usage
To run the spelling checker 
>  java -cp target/spell-checker-1.0-SNAPSHOT.jar challenge.SpellChecker

To run the mis-spelled word generator
>  java -cp target/spell-checker-1.0-SNAPSHOT.jar challenge.MisSpelledWordGenerator
  
# Solution for Spelling Checker
## Word Transformation
For all the words we do the following transformation to generate the pattern:
 
* Trim the word and change all characters to lower-case. 
* Remove all duplicate characters in the word. (ex: peel -> pel)
* Replace all vowels with * character. (ex: pel -> p*l)


## Load all Dictionary Words into the hash map
* 	Read the words in the dictionary line by line
*  Perform word transformation for each word loaded from the dictionary
*  Use word transformation pattern as the key of the hash map and value of the hash map is a list of dictionary words with the same pattern. 
	*  	ex: key: \*b\*t\*r   value: List[abater, abator] 
 
## Find Possible Candidates and Sort them
* Given a mis-spelled word, perform word transformation to find its pattern. 
* Search the pattern in the hash table. Since we removed the duplicate vowels in the word, we also want to repeat the starts in the pattern to find more possible candidates.
* After we find all possible candidates, we can use Edit distance to calculate the distance between each possible candidate and the mis-spelled word. Sort candidates by smaller edit distance. If the candidates have the same edit distance, we use english frequency ranking to help return more frequent word. (English frequency ranking downloaded from Wikipedia and stores in the hash map with key=work, value=rank paire)
* Return the first element in the recommendation list or "NO SUGGESTION" if the recommendation list is empty

## Time Complexity
* Initial dictionary loading: `O(n*m)` where n is the number of words in the dictionary and m is the length of words 
* Find the best recommendation work: `O(m)` where m is the length of the given word. (Usually there won't be too many candidates to compare)



# Solution for Generating Mis-Spelled Word
## Load all Dictionary Words into the Array List
* Read the words in the dictionary line by line and put into a array list. 
* We use this list to randomly pick up a word in memory.

## Randomly Pick the Word and Modify it
* Randomly pick up a word in the list loaded initially
* Randomly change the vowel in the word. The rules are:
	* If there are consecutive same vowels in the word, don't change the vowel. (ex: peel, keep vowel "ee")
	* If there are consecutive different vowels in the word, make sure they are still different after changing. (ex: eat can be changed to oat *BUT* can't change to eet)
* Randomly repeat the characters in the word several times. Maximum repeating time is 4.
* Randomly change the character to upper case.

## Verify the result
* In the IntegrationTest, generate a lot of mis-spelled words and make sure the suggestion list is always NOT empty.

