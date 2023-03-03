package com.cong.gitsync.model.domain


fun GitRepoBasicR.show() {
    for (repo in this.list()) {
        println("repo is ${repo.spec.Url} ${repo.Id} ${repo.spec.Branch} create ${repo.meta.CreateTime} ${repo.meta.UpdateTime}")
    }
}