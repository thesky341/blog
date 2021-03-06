package top.thesky341.blogapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import top.thesky341.blogapi.mapper.ArticleContentMapper;
import top.thesky341.blogapi.service.ArticleContentService;

import javax.annotation.Resource;

/**
 * @author thesky
 * @date 2020/12/5
 */
public class ArticleContentServiceImpl implements ArticleContentService {
    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Override
    public void deleteArticleContentById(int id) {
        articleContentMapper.deleteArticleContentById(id);
    }
}
