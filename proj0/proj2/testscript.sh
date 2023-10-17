javac gitlet/*.java
rm -rf .gitlet
java gitlet.Main init
echo "aaa" > a.txt
java gitlet.Main add a.txt
java gitlet.Main add ".gitignore"
java gitlet.Main commit "My first commit."
java gitlet.Main log
echo "bbb" > a.txt
java gitlet.Main add a.txt
java gitlet.Main commit "My second commit."
java gitlet.Main branch new
java gitlet.Main checkout new


