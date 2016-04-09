package rs.esolutions.ecomm.mock.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/**
 * Servlet implementation class UploadPhoto
 */
public class UploadPhoto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_LATITUDE = "latitude";
	public static final String PROPERTY_LONGITUDE = "longitude";
	public static final String PROPERTY_PHONE_NUMBER = "phoneNumber";
	public static final String PROPERTY_COMMENT = "comment";
	public static final String PROPERTY_PHOTO = "photo";

	/**
	 * Default constructor.
	 */
	public UploadPhoto() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		if (request.getHeader("Content-Type").contains("multipart/form-data")) {
			Map<String, String> fields = new HashMap<String, String>();
			Map<String, FileItem> photos = new HashMap<String, FileItem>();
			try {
				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem item : items) {
					if (item.isFormField()) {
						// Process regular form field (input type="text|radio|checkbox|etc", select, etc).
						String fieldName = item.getFieldName();
						String fieldValue = URLDecoder.decode(item.getString("UTF-8"), "UTF-8");
						fields.put(fieldName, fieldValue);
					} else {
						// Process form file field (input type="file").
						//String fieldName = item.getFieldName();
						String fileName = FilenameUtils.getName(item.getName());
						//InputStream fileContent = item.getInputStream();
						photos.put(fileName, item);
					}
				}
				out.println("<html><head><title> Response from uploading a photo</title>");
				out.println("</head><body><h1>Response from uploading a photo</h1>");
				for(String key : fields.keySet()) {
					out.println("<h2>Field: " + key +" , Value: " + fields.get(key) + " </h2>");
				}
				if(photos.entrySet().iterator().hasNext()) {
					out.println("<h2>Image size is " + photos.entrySet().iterator().next().getValue().getSize() +"</h2>");
				}
				out.println("</body></html>");
				out.close();
			} catch (FileUploadException e) {
				throw new ServletException("Cannot parse multipart request!", e);
			}
		}

	}
}