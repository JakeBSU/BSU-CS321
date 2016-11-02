# P4_Bioinfomatics
CS321 project 4 group repo

### Next steps:
1. Setup Git
	* If you're running Linux or OS X no worries. Git should already be install. To check run `git --version`. If you're running Windows then [Git for Windows](https://git-scm.com/downloads) is your best choice.
	* Tell Git your name so your commits will be properly labeled `git config --global user.name "YOUR NAME"`.
	* Tell Git the email address that will be associated with your Git commits `git config --global user.email "YOUR EMAIL ADDRESS"`.

2. Clone this repo
	* With Git setup move into a directory of your choice and run `git clone https://github.com/BigCheeze45/P4_Bioinfomatics.git`.

3. Create your branch

	To keep the project clean and have one stable code base we should each have our own branch.
	
	1.`cd P4_Bioinfomatics`.

	2.`git branch <branch name>` creates a new branch (for consistency, we should just use our name).

	3.`git checkout <branch name>` switch to that branch.
	
	4.`git push origin <branch name>` pushes your local branch to the GitHub repo.

	To check if you branched correctly refresh the GitHub page and you should see the number of branches (below the repo description) change. If your branch doesn't show up [here](https://github.com/BigCheeze45/P4_Bioinfomatics/branches) then give it a shot again.

### Workflow:
Since we'll all be workingo on our own branches it's important that we commit and push in the correct order so as not to overwrite previous work. So, to that end here are a couple things to keep in mind:    

1. Always work on and commit to your branch. See [this](https://confluence.atlassian.com/bitbucket/branching-a-repository-223217999.html) for a  walk-through.

2. Always do a `git pull origin master` before you start working. This gives you the latest version of all the files in this repo. This goes a long way in preventing overwriting code and hardwork!

3. After pushing to master, you'll want to reset your branch to be in line with master as instructed below. **This part is important. Please be sure to complete this step!**
Your workflow shoud be as follow:
    
    ```
    1. git checkout <your branch>
        ...git pull origin master
        ....do some work, commit, push...etc
    2. On GitHub submit a pull request for your branch
    3. Merge into master (if there are no conflicts). Once you've merged your branch will be behind 
    by a commit so you'll need to set it back to be inline with master. You do that as follow:
        1. git checkout master
        2. git pull
        3. git checkout <your branch>
        4. git rebase master
        5. git push
