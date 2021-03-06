package org.quickMap.apiservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.quickMap.base.BaseController;
import org.quickMap.constant.ApiServiceConstant.RoleList;
import org.quickMap.apiservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController()
@RequestMapping("/file")
public class OperatorController extends BaseController {

    @Autowired
    FileService fileService;

    /**
     * 下载
     *
     * @param
     * @return
     */
    @PreAuthorize("hasAnyRole('"+RoleList.ADMIN+"','"+RoleList.COMMON_USER+"')")
    @RequestMapping(value = "/{group}/{path}")
    public ResponseEntity<byte[]> download(@PathVariable("group") String group, @PathVariable("path") String path) {
        return fileService.download(group, path);
    }

    /**
     * 删除文件
     * @param delPath 根据参数删除文件
     * @return
     */
    @RequestMapping(value = "/file/del/{delPath}")
    String delete(@PathVariable("delPath") String delPath) {
        return fileService.delete(delPath);
    }

    /**
     * 上传
     *
     * @return
     */
    @PreAuthorize("hasAnyRole('"+RoleList.ADMIN+"','"+RoleList.COMMON_USER+"')")
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile[] files,@RequestParam(required = false)boolean thumbImage) {
        if (files.length == 0) {
            return failedRender();
        }
        JSONArray resp = new JSONArray();
        for (MultipartFile file : files) {
            String s = fileService.upload(file,thumbImage);
            resp.add(JSONObject.parse(s));
        }
        return successRenderData(resp);
    }

    /**
     * 上传base64
     *
     * @return
     */
    @PreAuthorize("hasAnyRole('"+RoleList.ADMIN+"','"+RoleList.COMMON_USER+"')")
    @PostMapping("/upload64")
    public String upload64(@RequestParam("b64") String b64, @RequestParam("fileName") String fileName,@RequestParam(required = false)boolean thumbImage) {
        return successRender(fileService.uploadB64(b64, fileName,thumbImage));
    }

    /**
     * 根据文件名称查询记录
     *
     * @return
     */
    @PreAuthorize("hasAnyRole('"+RoleList.ADMIN+"')")
    @RequestMapping("/search/exec")
    public String search(@RequestParam(value = "fileName",required = false) String fileName,@RequestParam(value = "before",required = false)Long before,@RequestParam(value = "after",required = false)Long after,@RequestParam(value = "suffix",required = false)String suffix){
        return fileService.search(fileName,before,after,suffix);
    }

    /**
     * 根据前缀获取完成建议
     *
     * @return
     */
    @PreAuthorize("hasAnyRole('"+RoleList.ADMIN+"')")
    @RequestMapping("/prefix/{prefix}")
    public String prefix(@PathVariable("prefix") String prefix) {
        return fileService.prefix(prefix);
    }


}
