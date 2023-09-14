echo "switching to master"

git checkout master

echo "building..."
#npm run build



echo "deploying files"
#scp -i "H:\New folder\downloads\qwe.pem" -r build/* ubuntu@43.205.119.40:/var/www/43.205.119.40/
pscp -i "C:\Users\as\sshkey\keypriv1.ppk" -r build/* ubuntu@3.111.198.52:/var/www/43.205.119.40/
echo "done"

