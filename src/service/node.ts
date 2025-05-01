import { triggerExport } from '@/tools/nodeApiHandle'; // Import the triggerExport function

export const exportdocxService = {
  /**
   * Handles exporting the data in DOCX format
   * @param query The query parameters to customize the export
   * @returns The exported DOCX file as a blob or relevant response
   */
  async exportDocx(query) {
    try {
      // Use the triggerExport function to send a POST request to the `export` endpoint
      const response = await triggerExport(query); // Use the query as data to be sent
      return response;
    } catch (error) {
      console.error('Error exporting DOCX:', error);
      throw error;
    }
  }
};
