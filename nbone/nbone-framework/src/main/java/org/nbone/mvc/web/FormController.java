package org.nbone.mvc.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import org.nbone.framework.spring.web.bind.annotation.ResultResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-21
 */
public interface FormController<T,Id> {

    @ApiOperation(value = "添加", notes = "使用Form表单参数方式请求"
            , extensions = @Extension(properties = @ExtensionProperty(name = "update", value = "1")))
    @FormPostMapping(value = {"", "add"})
    @ResultResponseBody
    boolean add(@Valid T entityRequest, HttpServletRequest request);


    @ApiOperation(value = "修改", notes = "REST风格,使用Form参数方式请求"
            , extensions = @Extension(properties = @ExtensionProperty(name = "update", value = "1")))
    @FormRequestMapping(method = RequestMethod.PUT)
    @ResultResponseBody
    boolean update(T entityRequest, HttpServletRequest request);

    @ApiOperation(value = "修改", notes = "传统URL风格,使用Form表单参数方式请求"
            , extensions = @Extension(properties = @ExtensionProperty(name = "update", value = "1")))
    @FormRequestMapping(value = "update", method = {RequestMethod.POST})
    @ResultResponseBody
    boolean updateAction(T entityRequest, HttpServletRequest request);


    @ApiOperation(value = "删除",notes = "REST风格,使用PathVariable参数方式请求")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResultResponseBody
    boolean delete(@PathVariable("id") Id id, HttpServletRequest request);

    @ApiOperation(value = "删除",notes = "传统URL风格")
    @RequestMapping(value = "delete", method = {RequestMethod.POST})
    @ResultResponseBody
    boolean deleteAction(@RequestParam("id") Id id, HttpServletRequest request);



    @ApiOperation(value = "查询详情信息",notes = "REST风格,使用PathVariable参数方式请求")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResultResponseBody
    Object get(@PathVariable("id") Id id, HttpServletRequest request);

    @ApiOperation(value = "查询详情信息",notes = "传统URL风格")
    @RequestMapping(value = {"","info"}, method = RequestMethod.GET)
    @ResultResponseBody
    Object info(@RequestParam("id") Id id, HttpServletRequest request);


}
